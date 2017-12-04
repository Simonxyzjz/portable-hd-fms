package simonxyzjz.phdfms.mongo.config;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import lombok.extern.slf4j.Slf4j;
import simonxyzjz.phdfms.mongo.shiro.MyShiroRealm;

@Slf4j
@Configuration
public class ShiroConfig {

    @Bean(name = "lifecycleBeanPostProcessor")  
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor(){  
        return new LifecycleBeanPostProcessor();  
    }

	@Bean
	public ShiroDialect shiroDialect() {
		return new ShiroDialect();
	}

	@Bean
	public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
		log.info("ShiroConfiguration.shirFilter()");
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		
		shiroFilterFactoryBean.setSecurityManager(securityManager);
		shiroFilterFactoryBean.setLoginUrl("/login");
		shiroFilterFactoryBean.setSuccessUrl("/fmsPage");
		shiroFilterFactoryBean.setUnauthorizedUrl("/403");
		Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();

		filterChainDefinitionMap.put("/logout", "logout");
		filterChainDefinitionMap.put("/css/**", "anon");
		filterChainDefinitionMap.put("/js/**", "anon");
		filterChainDefinitionMap.put("/img/**", "anon");
		filterChainDefinitionMap.put("/images/**", "anon");
		filterChainDefinitionMap.put("/easyui/**", "anon");
		filterChainDefinitionMap.put("/font-awesome/**", "anon");
		filterChainDefinitionMap.put("/favicon.ico","anon");
		filterChainDefinitionMap.put("/**", "authc");

		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
		return shiroFilterFactoryBean;
	}
	
	@Bean(name = "myShiroRealm")
    public MyShiroRealm myShiroRealm(EhCacheManager ehCacheManager){  
        MyShiroRealm realm = new MyShiroRealm();
        realm.setCacheManager(ehCacheManager);
        realm.setCredentialsMatcher(hashedCredentialsMatcher());
        return realm;  
    }
	
	@Bean
	public HashedCredentialsMatcher hashedCredentialsMatcher() {
		HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();

		hashedCredentialsMatcher.setHashAlgorithmName("md5");// 散列算法:这里使用MD5算法;
		hashedCredentialsMatcher.setHashIterations(2);// 散列的次数，比如散列两次，相当于 md5(md5(""));

		return hashedCredentialsMatcher;
	}

	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
		return authorizationAttributeSourceAdvisor;
	}

	@Bean
	public EhCacheManager getEhCacheManager() {
		EhCacheManager ehcacheManager = new EhCacheManager();
		ehcacheManager.setCacheManagerConfigFile("classpath:config/ehcache-shiro.xml");
		return ehcacheManager;
	}
	
	@Bean  
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator(){  
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();  
        creator.setProxyTargetClass(true);  
        return creator;  
    }
	
	@Bean(name = "securityManager")  
    public DefaultWebSecurityManager defaultWebSecurityManager(MyShiroRealm myShiroRealm){  
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(myShiroRealm);
        securityManager.setCacheManager(getEhCacheManager());
        return securityManager;  
    } 

}
