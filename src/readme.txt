2、com.entity.processing中是获取实体以及实体关系训练的特征向量。GetEntity为入口。
**
1、com.RE.nameEntityRecoMain：使用java调用bosonnlpAPI进行实体识别，
	结果存放到EntityAll文件夹中，为单独运行的一个包。
2、com.RE.pretreatment：对每一个公司对应的年报文章进行分词，存储