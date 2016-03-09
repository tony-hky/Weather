package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.apache.http.HttpConnection;

public class HttpUtil
{
	public static void sendHttpRequest(final String address,final HttpCallbackListener listener)
	{
		new Thread( new Runnable()
		{
			@Override
			public void run()
			{
				HttpURLConnection connection=null;
				try
				{
					URL url=new URL(address);
					connection=(HttpURLConnection) url.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(8000);
					connection.setReadTimeout(8000);
					InputStream in=connection.getInputStream();
					BufferedReader reader=new BufferedReader(new InputStreamReader(in));
					//String对象是不可改变的。每次使用 System.String类中的方法之一时，
					//都要在内存中创建一个新的字符串对象，这就需要为该新对象分配新的空间。
					//在需要对字符串执行重复修改的情况下，与创建新的 String对象相关的系统开销可能会非常昂贵。
					//如果要修改字符串而不创建新的对象，则可以使用System.Text.StringBuilder类。
					StringBuilder response =new StringBuilder();
					String line;
					while((line=reader.readLine())!=null)
					{
						response.append(line);
					}
					if (listener!=null)
					{
						//回调OnFinish方法
						listener.onFinish(response.toString());
					}
				} catch (Exception e)
				{
					if (listener!=null)
					{
						listener.onError(e);
					}
				}finally {
					if (connection!=null)
					{
						connection.disconnect();
					}
				}	
			}
		}) .start();
	}
}

