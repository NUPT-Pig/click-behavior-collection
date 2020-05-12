# 用户网页行为分析
## 简介
通过flume实时获取nginx的access请求，输出到kafka，spark-streaming
消费kafka中的access请求，进行分析，输出。
## TODO
永久存储到ES
status出错报警
## 其它
filebeat + ES  不能删选日志
不想用logstash 反正耗内存了，不如直接spark处理，还可以实时做一些任务。