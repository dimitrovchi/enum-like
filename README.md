# Memory consumption

|  Capacity|      Size|        Ha|        Id|        Ty|        Co|        Tr|        Sk|        Ar|
|----------|----------|----------|----------|----------|----------|----------|----------|----------|
|        16|         9|       576|       472|       248|       264|       568|       600|       232|
|        64|         9|       768|      1240|       440|       456|       568|       600|       232|
|        64|        50|      2992|      1896|      1096|      1112|      2864|      2680|       920|
|        70|         9|      1024|      1240|       464|       480|       568|       600|       232|
|        70|        50|      2992|      1896|      1120|      1136|      2864|      2680|       920|
|        70|        70|      3952|      2216|      1440|      1456|      3984|      3696|      1272|

Map types:

* Ha - HashMap-based
* Id - IdentityHashMap-based
* Ty - array by ordinals
* Co - concurrent array by ordinals
* Tr - TreeMap based
* Sk - ConcurrentSkipListMap-based
* Ar - sorted arrays map

# Performance

| Map | 'Put' Time, ns |
|-----|----------------|
| Ha  |        32.121  |
| Co  |        14.271  |
| Sk  |        97.286  |
| Tr  |        43.881  |
| Ty  |         7.180  |
| Ar  |       145.274  |
