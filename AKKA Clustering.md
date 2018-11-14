## Cluster详述
> #### 注意
> 本文档描述了Akka Cluster的设计思想。翻译自：
> https://doc.akka.io/docs/akka/2.5/common/cluster.html

### 介绍
Akka Cluster提供了一个容错的(fault-tolerant)、去中心化的(decentralized)、点对点的(peer-to-peer based)集群[成员](#membership)服务，排除了单点故障或单点瓶颈。使用[gossip协议](#gossip)和一个自动[故障检测器](#failure detector)来做到这一点。

Akka cluster allows for building distributed applications, where one application or service spans multiple nodes (in practice multiple ActorSystems). See also the discussion in [When and where to use Akka Cluster](https://doc.akka.io/docs/akka/2.5/cluster-usage.html#when-and-where-to-use-akka-cluster).

### 术语
**节点(node)**

集群的逻辑成员。一台物理机器上可能有多个节点。节点由一个 *hostname:port:uid* 元组定义

**集群(cluster)**

通过成员服务连接在一起的一组节点

**领导者(leader)**

由集群中的单个节点作为领导者。它会管理集群的收敛(convergence)和成员状态的转换。

### <span id="membership">成员</span>
集群由一组成员节点组成。每个节点的标识符是一个`hostname:port:uid`的元组。一个Akka的应用程序可以分布运行于集群的每个节点上。集群的成员和运行于节点上的Actors是解耦的。一个集群成员节点可以不运行任何的Actor。只需要向集群中的任一节点发送一个`Join`指令就可以加入一个集群。

标识符中包含了一个UID，该UID唯一标识机器`hostbane:port`上的一个actor system实例。Akka使用这个UID可以监视远程实例死亡，这意味着一个actor system一旦被移出集群，就不能再次加入了。想要让一个机器`hostbane:port`上actor system实例再次加入该集群，你必须停止该实例并启动一个新的实例，这也将会让它拥有一个不同的UID。

The cluster membership state is a specialized [CRDT](https://hal.inria.fr/file/index/docid/555588/filename/techreport.pdf), 这意味着它仅有一个单调的merge方法。当在不同节点上发生并发更改时，可以始终合并更新成一个相同的结果。

###### <span id="gossip">Gossip</span>
Akka的集群成员是基于Amazon的[Dynamo](https://www.allthingsdistributed.com/files/amazon-dynamo-sosp2007.pdf)系统，特别是Basho的[Riak](http://basho.com/technology/architecture/)分布式数据库中采用的方法。集群当前的状态信息通过[Gossip协议](http://en.wikipedia.org/wiki/Gossip_protocol)随机传达给集群中的成员，而且会偏向于告知未知道最新状态的成员。

###### Vector Clocks 向量时钟
[Vector Clocks](http://en.wikipedia.org/wiki/Vector_clock)是一种数据结构和算法，用于在分布式系统中生成事件的部分排序，并检测因果关系的违规。

我们使用Vector Clocks来协调和合并使用Gossip通讯期间群集状态的差异。Vector Clocks是一个(节点，计数器)对。对集群状态的每次更新都伴随着向量时钟的更新。

###### Gossip Convergence
集群的信息会在某个时间点在一个节点汇聚。这时该节点可以证明群集中的所有其他节点已观察到他正在观察的群集状态。在gossip期间看到当前集群状态版本的节点的集合，可以被称为gossip概览中的可见集。通过传递该集合信息，来实现收敛。当所有节点都包含在该集合中时，存在收敛。

任何节点在`unreachable`时都不会发生gossip收敛。节点需要再次变成`reachable`，或者变成`down`和`removed`状态(详见下面的[成员生命周期]()章节)。这仅组织了Leader执行集群成员管理，但并不会影响在集群顶部运行的应用程序。For example this means that during a network partition it is not possible to add more nodes to the cluster. The nodes can join, but they will not be moved to the `up` state until the partition has healed or the unreachable nodes have been downed.

###### <span id="failure detector">Failure Detector</span>
