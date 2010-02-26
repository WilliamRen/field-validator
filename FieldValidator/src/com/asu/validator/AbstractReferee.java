package com.asu.validator;

import java.lang.annotation.Annotation;
import java.util.regex.Pattern;

public abstract class AbstractReferee<T extends Annotation> implements Referee {

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
	 * 
	 */
	public abstract State check(Object data) ;
}
