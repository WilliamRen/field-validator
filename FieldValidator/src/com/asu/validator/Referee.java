package com.asu.validator;

import java.lang.annotation.Annotation;


/**
 * �����
 * 
 * ���м����ʵ�־�Ҫʵ�ִ˽ӿ�
 * 
 * @author Bevis.Zhao
 */
public interface Referee{
	
	/**
	 * �����ݽ���У��
	 * @param instance TODO
	 * @param data ���������
	 * @param annotation ����ע��
	 * @param fieldName �ֶ�����
	 * 
	 * @return У����
	 */
	State check(Object instance, Object data, Annotation annotation, String fieldName);
	
}
