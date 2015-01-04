package libraryInterface;

/**
 * 
 * @author ZYL
 * Library是对所有词库的一个抽象，
 * 这个类是对具体一个词库的抽象,
 * 在包外，使用词库时用Library类型
 * 的引用即可。
 * 下面是词库结构的说明：
 * 词库由两个文件组成，一个装有单词头，一个装有解释
 * 单词头由单词和其解释在文件中的代表行数的index组
 * 成。两个文件都是一行写一条。装有解释的文件要求每
 * 行长度相等，以便查找。 
 * 在程序里，library的实例中含有装有系列单词头的
 * ArrayList，查找解释时进行文件读写，读出解释
 */
class LibraryInstance extends Library
{
	

}
