# 项目结构描述
```
├─ proxy-api：对于被代理服务的相关api，如在dubbo网关上修饰请求方式的 ProxyMethod，这些注解都需要直接加在服务接口上
├─ proxy-core：代理网关核心模块，与代理网关逻辑及预制网关框架
├─ proxy-utils：工具模块
├─ proxy-web-support：对pring mvc的支持，并且实现基本的 HttpServletRequest、HttpServletResponse 的包装，提供最基本的http入口
├─ dubbo-proxy：dubbo网关
```

# 代理规则
## 参数规则
在代理网关中，将参数分成三种数据来源：
1. 请求体
2. 请求参数：在web时，即为query param
3. 附加参数：在web时，即为请求头header

并且为了兼容部分无法获取参数名称的上游服务，因此提供```name```和```index```搭配使用，来表明入参列表位置及名称。
例如目前的dubbo网关，由于元数据中心为上报参数信息，因此使用案例如下：
```java
public interface UserService {
    @ProxyMethod(method = ProxyMethodType.PUT)
    @ProxyParam(index = 0, name = "id")
    @ProxyParam(index = 1, name = "user")
    void updateById(Long id, User user);
}
```
上面例子中，通过多个 ```ProxyParam``` 注解，来描述```id```和```user```的参数位置及名称，这样在网关中即可根据参数列表来进行处理
