# AkkaStudy
#### Akka 路径  
当我们创建一个ActorSystem的时候,AKKA会为该System默认的创建三个Actor,并处于不同的层次:  
```
root guardian(/)
├── guardian(user)
│   └─── your actor hierarchy:
└── system guardian(system)
    └─── sys actor hierarchy
```
本地Actor  
akka://sys-name/user/...  
akka://sys-name/system/...  

#### Actor创建  
* **ActorSystem创建**  
```
ActorSystem.create("sys-name");  
ActorSystem.create("sys-name", Config config); 
```
* **Props创建**
```
Props.create(Class class);  
Props.create(Class class, Object... object);  // object为class的构造参数
Props.create(Class class).withDispatcher("dispatcher-config"); 
```
* **Actor创建**
```
system.actorOf(Props props, "actor-name");
```

#### Akka 生命周期  
单个Actor的生命周期：  
actorOf -> preStart -> start -> receive -> stop -> postStop

#### Dispatcher
当Actor的数量比较多后,彼此之间的通信就需要协调,从而能更好的平衡整个系统的执行性能.  
在AKKA中,负责协调Actor之间通信的就是Dispatcher.它在自己独立的线程上不断的进行协调,把来自各个Actor的消息分配到执行线程上.
在AKKA中提供了四种不同的Dispatcher,我们可以根据不同的情况选择不同的Dispatcher:
* `Dispatcher`:这个是AKKA默认的Dispatcher.对于这种Dispatcher,每一个Actor都由自己的MailBox支持,它可以被多个Actor所共享.而Dispatcher则由`ThreadPool`和`ForkJoinPool`支持.比较适合于非阻塞的情况.  
**thread-pool-executor / fork-join-executor参数:**
```
core-pool-size-min/parallelism-min : 最小线程数
core-pool-size-max/parallelism-max : 最大线程数
core-pool-size-factor/parallelism-factor: 线程层级因子,通常和CPU核数相关.
```
* `PinnedDispatcher`:这种Dispatcher为每一个Actor都单独提供了专有的线程,这意味着该Dispatcher不能再Actor之间共享.因此,这种Dispatcher比较适合处理对外部资源的操作或者是耗时比较长的Actor.PinnedDispatcher在内部使用了ThreaddPool和Executor,并针对阻塞操作进行了优化.所以这个Dispatcher比较适合阻塞的情况.但是在使用这个Dispatcher的时候需要考虑到线程资源的问题,不能启动的太多.
* `BalancingDispatcher(已被废弃)`:它是基于事件的Dispatcher,它可以针对相同类型的Actor的任务进行协调,若某个Actor上的任务较为繁忙,就可以将它的工作分发给闲置的Actor,前提是这些Actor都属于相同的类型.对于这种Dispatcher,所有Actor只有唯一的一个MailBox,被所有相同类型的Actor所共享.
* `CallingThreadDispatcher`:这种Dispatcher主要用于测试,它会将任务执行在当前的线程上,不会启动新的线程,也不提供执行顺序的保证.如果调用没有及时的执行,那么任务就会放入ThreadLocal的队列中,等待前面的调用任务结束后再执行.对于这个Dispatcher,每一个Actor都有自己的MailBox,它可以被多个Actor共享.
