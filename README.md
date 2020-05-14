# 用户网页行为分析
## 简介
通过flume实时获取nginx的access请求，输出到kafka，spark-streaming
消费kafka中的access请求，进行分析，输出。
## TODO
永久存储到ES
status出错报警
## 其它
docker elk (logstash + ES + kibana) 可以做
这里主要关注实时分析的特性