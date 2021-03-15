use manual

1、ensure you have configured `HIVE_HOME` in your environment

2、copy your `atlas-application.properties `into  `$HIVE_HOME/conf/`

3、then untar the file `atlas-hive-hook-2.1.0_2.3.3.tar.gz` in any path you link

4、you should assign the path you have untared on the third step to an environment variable call `HIVE_AUX_JARS_PATH` on your classpath ,eg I have untared it in path `/opt/atlas-hive-hook-2.1.0_2.3.3`

then I sholud configure like this

```shell
export HIVE_AUX_JARS_PATH=/opt/atlas-hive-hook-2.1.0_2.3.3
```

5、add below config segment into your `hive-site.xml` configure file

```xml
<name>hive.exec.post.hooks</name>
	<value>org.apache.atlas.hive.hook.HiveHook</value>
</property>
```

Congratulations on enjoying your journey of atlas

appendix:

```shell
[root@CentOSB atlas-hive-hook-2.1.0_2.3.3]# tree -L 2
.
├── atlas-bridege-shim-2_3_3-2.1.0.jar
├── atlas-hive-plugin-impl
│   ├── atlas-hive-bridge-2_3_3-2.1.0.jar 
│   └── lib # all dependencies that atlas-hive-bridge will use 
├── atlas-plugin-classloader-2.1.0.jar 
└── import-hive.sh   # to load your history metadata into atlas

2 directories, 4 files

```

**Note**: Please keep in mind do not change the directory construct！！you can specifiy `atlas.metadata.namespace=5k` configure as extra properties in `atlas-application.properties` file,then all the message send from this hive node will come from `5k` cluster.
