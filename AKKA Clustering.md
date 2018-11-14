## Cluster详述
> #### 注意
> 本文档描述了Akka Cluster的设计思想。

### 介绍
Akka Cluster提供了一个容错的(fault-tolerant)、去中心化的(decentralized)、点对点的(peer-to-peer based)集群[成员](#jump)服务，排除了单点故障或单点瓶颈。使用gossip协议和一个自动故障检测器来做到这一点。

### 术语

### <span id="jump">成员</span>

###### Gossip

