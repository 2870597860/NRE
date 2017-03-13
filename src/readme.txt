2、com.entity.processing中是获取实体以及实体关系训练的特征向量。GetEntity为入口。
**
1、com.RE.nameEntityRecoMain：使用java调用bosonnlpAPI进行实体识别，
	结果存放到EntityAll文件夹中，为单独运行的一个包。
2、com.RE.pretreatment：对每一个公司对应的年报文章进行分词，存储
3、com/RE/RealtionPattern：进行xml分析获取句子模式，并与种子进行匹配

////////////////////////////
cn.ner包下的程序是重新组织好的：dpmain是最句子进行依存分析的，由于分析平台因素，需要贩毒运行人工进行调整；
main1是平面特征获取以及分类，同时获取要依存分析的句子。
main3要在main2获取数据结束后进行匹配。

DPCache为缓存目录：因为ltp依存分析有一定的bug，所有需要一个一个进行分析
DPSentence_will:存放要依存分析的文件，将本目录中的文本一个个存到DPCache中进行分析