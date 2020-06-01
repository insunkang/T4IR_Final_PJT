package mongoServer;

import java.io.File;
import java.io.IOException;

import rcaller.RCaller;
import rcaller.RCode;

public class WordCloud {
	public WordCloud() {
		try {
			RCaller caller=new RCaller();
			caller.setRscriptExecutable("F:/Rdata/R-4.0.0/bin/x64/Rscript.exe");
			RCode code=new RCode();
			code.clear();
			
			
			File file = null;
			file=code.startPlot();
			System.out.println(file);
			
			code.addRCode("library(usethis)");
			code.addRCode("library(devtools)");
			code.addRCode("library(KoNLP)");
			code.addRCode("library(multilinguer)");
			code.addRCode("library(RColorBrewer)");
			code.addRCode("library(wordcloud)");
			code.addRCode("library(stringr)");
			
			
			code.addRCode("request<-read.csv(\"F://mongtcsv.csv\", header = FALSE, stringsAsFactors = FALSE,fileEncoding = \"utf-8\")");
			code.addRCode("sampledata<-request$V1[1:700]");
			code.addRCode("data_list<- list()");
			code.addRCode("for(i in 1:length(sampledata)){data<-SimplePos09(sampledata[i]); data_list[[i]]<-data}");
			code.addRCode("unlist<-unlist(data_list)");
			code.addRCode("wordlist<-sapply(str_split(unlist,\"/\"),function(x){x[1]})");
			code.addRCode("tablewordlist<- table(wordlist)");
			code.addRCode("sort(tablewordlist,decreasing = T)[1:100]");
			code.addRCode("tablewordlist_result<-tablewordlist[nchar(names(tablewordlist))>1]");
			code.addRCode("tablewordlist_result<-sort(tablewordlist_result,decreasing = T)[1:100]");
			code.addRCode("word<-names(tablewordlist_result)");
			code.addRCode("count<-as.numeric(tablewordlist_result)");
			code.addRCode("windows()");
			code.addRCode("mycolor<-brewer.pal(n=11,name =\"Set1\")");
			code.addRCode("wordcloud(words = word,freq = count,random.order = F,colors = mycolor)\r\n");
			code.addRCode("savePlot(\"F://wordcloud.png\",type = 'png')");
			code.endPlot();
			caller.setRCode(code);
			caller.runOnly();
			//code.showPlot(file);
			//File folder=new File("F://");
			file.delete();
			

			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	}
}
