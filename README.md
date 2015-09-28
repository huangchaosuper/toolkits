# toolkits

kinds of toolkits
========================

# elasticsearch export

java -DconfigPath ./<path>/elasticsearch-export.properties -jar elasticsearch-<version>.jar 

```Shell
#####################main####################
main=cn.huangchaosuper.toolkits.SimpleTask

#####################SimpleTask####################
simpletask.package=cn.huangchaosuper.toolkits.elasticsearch.export

#####################elasticsearch####################
elasticsearch.cluster.name=elasticsearch
elasticsearch.transport.address=localhost
elasticsearch.index.name=logstash-2015.09.24
```