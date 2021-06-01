package io.github.cloudadc.sslo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
public class F5SSLOTopologiesGenerator implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(F5SSLOTopologiesGenerator.class, args);
	}
	
	static String getResourceFileAsString(String fileName) throws IOException {
		
		
		String content = Files.readString(Paths.get(fileName), StandardCharsets.US_ASCII);
		
		if(content == null || content.length() == 0) {
			throw new RuntimeException(fileName + " not exist under template");
		}
	    
		return content;
	}
	
	public static void recursiveDeletion(File file) {
		
		if (!file.exists()) {
			return;
		}
		
		if (file.isDirectory()) {
			for (File f : file.listFiles()) {
				recursiveDeletion(f);
			}
		}
		
		file.delete();
	}
	

	@Override
	public void run(String... args) throws Exception {
		
		
		if(!Files.exists(Paths.get("apiPost.json")) && args.length == 0) {
			throw new RuntimeException("Either pass 'apiPost.json' via args, or put to app home");
		}
		
		String config = getResourceFileAsString("apiPost.json");
		
		if(args.length >= 1) {
			config = getResourceFileAsString(args[0]);
		}
		
		ObjectMapper mapper = new ObjectMapper();
		final Topologies obj = mapper.readValue(config.getBytes(), Topologies.class);
		
		//TypeReference<List<Topology>> typeReference = new TypeReference<List<Topology>>(){};
		
		//List<Topology> topologies = mapper.readValue(config.getBytes(), typeReference);
		
		List<Topology> topologies = obj.getTopologies();

		final String origin = load("api.topology.waf");
		
		final String sslo_ssl_ppol = load("api.ssl.pool");
		
		final String sslo_ssl_vs = load("api.ssl.vs");
		
		final String sslo_yewu_pool = load("api.yewu.pool");
		
		topologies.forEach( t -> {
			
			String name = t.getName();
			try {
				recursiveDeletion(Paths.get("topology", name).toFile());
				Files.createDirectory(Paths.get("topology", name));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			
			String vlan = t.getVlan();
			
			if(!vlan.endsWith("/24")) {
				throw new RuntimeException("Current only support /24 network");
			}
			
			String ip_addr = vlan.substring(0, vlan.length() - 3);
			String[] array = ip_addr.split("[.]", 0);
			String ip_pre = array[0] + "." + array[1] + "." + array[2];
			String ip_last = array[3];
			
			
			for(int i = Integer.parseInt(ip_last) + 1 ; i < 254 ; i ++) {
				String topology_name = name + "_" + i;
				String ip = ip_pre + "." + i ;
				String sslPoolName = "SSL_POOL_" + topology_name;
				String sslVSName = "SSL_VS_" + topology_name;
				String yewuPoolName = "YEWU_POOL_" + topology_name;
				System.out.println("    Generating topology " + topology_name + ", ip is " + ip + ", ssl pool " + sslPoolName + ", ssl vs " + sslVSName + ", yewu pool " + yewuPoolName);
				try {
							
					String ssl_pool = sslo_ssl_ppol.replaceAll("SSL_GENERATOR_POOL", sslPoolName);
					Files.write(Paths.get("topology", name, sslPoolName), ssl_pool.getBytes(), StandardOpenOption.CREATE_NEW);
					
					String ssl_vs = sslo_ssl_vs.replaceAll("SSL_GENERATOR_VS_NAME", sslVSName);
					ssl_vs = ssl_vs.replaceAll("SSL_GENERATOR_VS_IP", ip);
					ssl_vs = ssl_vs.replaceAll("SSL_GENERATOR_VS_POOL", sslPoolName);
					Files.write(Paths.get("topology", name, sslVSName), ssl_vs.getBytes(), StandardOpenOption.CREATE_NEW);
					
					String yewu_pool = sslo_yewu_pool.replaceAll("YEWU_GENERATOR_POOL", yewuPoolName);
					Files.write(Paths.get("topology", name, yewuPoolName), yewu_pool.getBytes(), StandardOpenOption.CREATE_NEW);
					
					String topology = origin.replaceAll("TOPOLOGY_GENERATOR_NAME", topology_name).replaceAll("TOPOLOGY_GENERATOR_VS_IP", ip);
					topology = topology.replaceAll("TOPOLOGY_GENERATOR_YEWU_POOL", yewuPoolName);
					Files.write(Paths.get("topology", name, topology_name), topology.getBytes(), StandardOpenOption.CREATE_NEW);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
				
			}
			
			try {
				String bash = load("apiPost.sh");
				bash = bash.replaceAll("TOPOLOGY_GENERATOR_BASH_NAME", name);
				bash = bash.replaceAll("TOPOLOGY_GENERATOR_BASH_INTERVAL", String.valueOf(obj.getInterval()));
				Files.write(Paths.get("topology", name, "apiPost.sh"), bash.getBytes(), StandardOpenOption.CREATE_NEW);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			
			System.out.println();
			
		});
		
		
	}
	
    public static String load(String name) throws IOException  {
		
		Resource resource = new ClassPathResource("classpath:" + name);
		if (resource.exists()) {
			try (InputStream inputStream = resource.getInputStream()){
				byte[] bdata = FileCopyUtils.copyToByteArray(inputStream);
				return new String(bdata, StandardCharsets.UTF_8);
			} catch (IOException e) {
				throw e;
			}
		} else {
			try (InputStream inputStream = new FileInputStream(new File("src/main/resources/" + name))){
				byte[] bdata = FileCopyUtils.copyToByteArray(inputStream);
				return new String(bdata, StandardCharsets.UTF_8);
			} catch (IOException e) {
				
				throw e;
			}
		}
		
		
		
	}
	
	

	
}
