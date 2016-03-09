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
					//String�����ǲ��ɸı�ġ�ÿ��ʹ�� System.String���еķ���֮һʱ��
					//��Ҫ���ڴ��д���һ���µ��ַ������������ҪΪ���¶�������µĿռ䡣
					//����Ҫ���ַ���ִ���ظ��޸ĵ�����£��봴���µ� String������ص�ϵͳ�������ܻ�ǳ�����
					//���Ҫ�޸��ַ������������µĶ��������ʹ��System.Text.StringBuilder�ࡣ
					StringBuilder response =new StringBuilder();
					String line;
					while((line=reader.readLine())!=null)
					{
						response.append(line);
					}
					if (listener!=null)
					{
						//�ص�OnFinish����
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
