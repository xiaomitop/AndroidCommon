# AndroidCommon

这个为方便自己快速开发android的一些基础工具，减少开发中的重复工作。  
1.收录了一些工具类，如StringUtils、FileUtil等  
1.异常处理统一返回log路径，便于上传服务器等操作    
2.注解框架，使用ButterKnife，统一在ACBaseActivity和ACBaseFragment配置，继承这两个后不用再去注册和取消。  
3.ACActivityStack统一管理activity，摘自 http://www.kymjs.com  
4.HTTP框架，使用的是OkHttp，封装是修改自 http://www.kymjs.com volley二次封装的请求框架，去掉了volley的一些操作，如缓存、队列请求。采用okhttp自身的缓存等。   
5.接入常用的侧滑框架 https://github.com/jfeinstein10/SlidingMenu  