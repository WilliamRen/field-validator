package com.asu.validator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;

/**
 * 
 * ����У��������ʵ�֣��ṩ��������
 * 
 *
 * @author Bevis.Zhao
 * @date 2010-6-3
 * @param <T>
 */
public abstract class AbstractReferee<T extends Annotation> implements Referee {
	
	// ��properties�ļ��ж�ȡ����Ϣ
	public final static Map<String, String> VALIDATE_MESSAGES = new HashMap<String, String>();
	
	static {
		// ��ʼ����ȡ�����ļ�
		Configuration settings = null;
		try {
			settings = new PropertiesConfiguration("field-validator_messages.properties");
		} catch (Exception e) {
			// ��ȡʧ�ܣ�������
		}
		
		if (settings != null) {
			Iterator<?> keys = settings.getKeys();
			
			while (keys.hasNext()) {
				String key = keys.next().toString();
				String message = settings.getProperty(key).toString();
				VALIDATE_MESSAGES.put(key, message);
			}
		}
	}
	/**
	 * ��ȡMessage�����ȴ�rule�ж�ȡ��
	 * 
	 * ����Ҳ������ڲ�ѯproperties��ȡ��ֵ
	 * 
	 * @param key
	 * @return
	 */
	protected String getMessageRuleFirst(String key, String def){
		
		// ���ȶ�ȡruleMessage
		String ruleMessage = (String)invokeMethod(rule, "message");
		return !StringUtils.isEmpty(ruleMessage) 
			? ruleMessage.trim()
			: getMessage(key, def);
	}
	
	/**
	 * ��properties�ļ��ж�ȡmessage�����û����ʹ��def
	 * 
	 * @param key
	 * @param def
	 * @return
	 */
	protected String getMessage(String key, String def){
		String found = VALIDATE_MESSAGES.get(key);
		return found == null ? def : found;
	}
	
	// ����
	protected T rule;
	// Beanʵ��
	protected Object instance;
	// У���ֶε��ֶ���
	protected String fieldName;
	
	private final static State SIMPLE_SUCCESS =  new State(true, "");
	/**
	 * �����򵥵ĳɹ�״̬��State���� 
	 */
	protected final static State simpleSuccess(){
		return SIMPLE_SUCCESS;
	}
	
	/**
	 * �����ɹ�����
	 * @param message
	 * @return
	 */
	protected final static State success(String message){
		return new State(true, message);
	}
	
	/**
	 * ����ʧ�ܶ���
	 * @return
	 */
	protected final static State failure(String message){
		return new State(false, message);
	}
	
	/**
	 * ����������� 
	 */
	@SuppressWarnings("unchecked")
	protected void setup(Object instance, Annotation annotation, String fieldName){
		this.instance = instance;
		this.rule = (T)annotation;
		this.fieldName = fieldName;
	}
	
	/**
	 * �Զ�����Null���
	 */
	public State check(Object instance, Object data, Annotation annotation, String fieldName) {
		setup(instance, annotation, fieldName);
		if(data == null)
			return simpleSuccess();
		return check(data);
	}
	/**
	 * ������ 
	 */
	protected final State regexMatch(String regex, Object data, String errorMessage){
		if(Pattern.matches(regex, (String)data))
			return simpleSuccess();
		else
			return failure(errorMessage);
	}

	/**
	 * ���÷���
	 *  
	 * @param object
	 * @param methodName
	 * @param parameters
	 * @return
	 */
	private static Object invokeMethod(Object object, String methodName, Object... parameters){
		Class<?>[] parameterTypes = new Class<?>[parameters.length];
		for(int i = 0; i < parameters.length; i++){
			parameterTypes[i] = parameters[i].getClass();
		}
		Method method = null;
		try {
			method = object.getClass().getMethod(methodName, parameterTypes);
			return method.invoke(object, parameters);
		} catch (Exception e) {
			throw new RuntimeException(String.format("������<%s>����<%s>ʧ�ܣ�", 
					object.getClass(), method));
		}
	}
	/**
	 * 
	 */
	public abstract State check(Object data) ;
}
