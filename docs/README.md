# Welcome to Rate Limiter
## About Rate limiter
    Rate limiter will limit the number of calls for a system. 
    If a user or macine request for API more then the specified limit, then the request will not be processed.
## Use
    It prevents excessive use of the system by client. It helps minimizing latency in a distributed system.
## How to use
    Add below dependency
```xml
<dependency>
   <groupId>com.sekhar.rate.limiter</groupId>
   <artifactId>rate-limiter</artifactId>
   <version>${version.number}</version>
</dependency>
```
## Rate Limiter can be configured based on need
    Rate limiter can be configured with properties like below
    rate-limiter:
        unit: SECOND
        limit: 3
    unit can be SECOND or MINUTE
    limit - the number of request for a unit
    example - in the above configuration 3 request are allowed per second.
    by deffault the configuration will be enabled to disable add below configuration in properties
        rate-limiter:
            enabled: false
## Design
    If Rate limiter is set to N request per second then accessing APIs more then N request will not be processed
    and 429 (Too many request) status code will be sent to client.