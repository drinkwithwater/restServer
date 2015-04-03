package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;

public class AsyncClient {
	public static void main(String args[]) throws InterruptedException, ExecutionException, IOException{
		runGet();
	}
	public static void runPost(){
		CloseableHttpAsyncClient client=HttpAsyncClients.createDefault();
		client.start();
		HttpPost request=new HttpPost("http://127.0.0.1:8081");
		try {
			StringEntity entity=new StringEntity("fds");
			request.setEntity(entity);
			client.execute(request,null);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}
	public static void runGet() throws InterruptedException{
		CloseableHttpAsyncClient client=HttpAsyncClients.createDefault();
		client.start();
		final HttpGet request1=new HttpGet("http://www.renren.com");
		final HttpGet request2=new HttpGet("http://59.0.0.1:8081");
		Future<HttpResponse> future1=client.execute(request1,null);
		RequestConfig config=RequestConfig.custom()
				.setConnectTimeout(2000)
				.setSocketTimeout(2000)
				.build();
		request2.setConfig(config);
		/*
		HttpResponse re=future1.get();
		System.out.println(re.getStatusLine());*/
		Future<HttpResponse> future2=client.execute(request2,new FutureCallback<HttpResponse>(){
			@Override
			public void cancelled() {
				System.out.println("cancelled");
			}
			@Override
			public void completed(HttpResponse arg0) {
				System.out.println(arg0.getStatusLine());
				try {
					InputStreamReader reader=new InputStreamReader(arg0.getEntity().getContent());
					BufferedReader br=new BufferedReader(reader);
					String rd="";
					while(rd!=null){
						rd=br.readLine();
						if(rd==null) break;
                        System.out.println(rd);
					}
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			@Override
			public void failed(Exception arg0) {
				System.out.println("failed");
			}
		});
		System.out.println("hello world");

	}
}
