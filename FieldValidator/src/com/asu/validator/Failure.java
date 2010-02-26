package com.asu.validator;

import java.io.Serializable;
import java.lang.annotation.Annotation;

/**
 * У��ʧ����Ϣ��
 * @author Bevis.Zhao(avengerbevis@gmail.com)
 *
 */
public class Failure implements Serializable{
	private static final long serialVersionUID = 1L;

	private Class<? extends Annotation> rule;
	private String message;
	private Integer status ;
	
	public static Failure valueOf(Class<? extends Annotation> rule, String message, Integer status){
		return new Failure(rule, message, status);
	}
	
	private Failure(Class<? extends Annotation> rule, String message, Integer status){
		this.rule = rule;
		this.message = message;
		this.status = status;
	}

	public Class<? extends Annotation> rule() {
		return rule;
	}

	public String message() {
		return message;
	}

	public Integer status() {
		return status;
	}
	
	/**
	 * Equals�ж�, Ϊ���ڼ����п���contains����
	 * ����equals����, ����ʹ��Rule�����ж�
	 * ��Rule��ͬʱ, ����true;
	 * 
	 */
	@Override
	public String toString(){
		return String.format("Failure > rule : %s, message : %s, status : %s", rule.getName(), message, status);
	}
	
	/**
	 * �ж�Failure�ļ������Ƿ��������ָ��Rule��Failure
	 * 
	 * @param iterable ʧ����ϢFailure����
	 * @param rule
	 * @return
	 */
	public static boolean containsRule(Iterable<Failure> iterable, Class<? extends Annotation> rule){
		for(Failure f : iterable)
			if(f.rule == rule)
				return true;
		return false;
	}
}
