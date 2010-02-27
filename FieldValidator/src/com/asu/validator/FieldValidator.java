package com.asu.validator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.asu.validator.referee.ChineseReferee;
import com.asu.validator.referee.DateTimeReferee;
import com.asu.validator.referee.EarlierThanReferee;
import com.asu.validator.referee.EnglishReferee;
import com.asu.validator.referee.EqualsReferee;
import com.asu.validator.referee.GreaterThanReferee;
import com.asu.validator.referee.LengthReferee;
import com.asu.validator.referee.LessThanReferee;
import com.asu.validator.referee.NonEqualsReferee;
import com.asu.validator.referee.NonNullReferee;
import com.asu.validator.referee.NumberReferee;
import com.asu.validator.referee.RegexReferee;
/**
 * 
 * ��У����
 *
 * @Singleton
 * @author Bevis.Zhao(avengerbevis@gmail.com)
 * 
 */
@SuppressWarnings("deprecation")
public class FieldValidator {
	// Singleton
	protected FieldValidator() {
	}
	// ������У����ӳ��
	private final static Map<Class<? extends Annotation>, Referee> rule2RefereeMap = new ConcurrentHashMap<Class<? extends Annotation>, Referee>();
	// ���򻺴�
	private final static Map<Class<?>, Map<Field, Annotation[]>> cachedRulesMap = new ConcurrentHashMap<Class<?>, Map<Field,Annotation[]>>();
	
	// ��ʼ����ģʽΪ��Ʒģʽ����ģʽ
	private static MODE currentMode = MODE.PRODUCT;
	
	static {
		// ��ʼ��Ĭ�Ϲ���У����
		registerReferee(Rule.NonNull.class, NonNullReferee.class);
		registerReferee(Rule.Equals.class, EqualsReferee.class);
		registerReferee(Rule.NonEquals.class, NonEqualsReferee.class);
		registerReferee(Rule.Length.class, LengthReferee.class);
		registerReferee(Rule.Chinese.class, ChineseReferee.class);
		registerReferee(Rule.English.class, EnglishReferee.class);
		registerReferee(Rule.Num.class, NumberReferee.class);
		registerReferee(Rule.Regex.class, RegexReferee.class);
		registerReferee(Rule.DateTime.class, DateTimeReferee.class);
		registerReferee(Rule.GreaterThan.class, GreaterThanReferee.class);
		registerReferee(Rule.LessThan.class, LessThanReferee.class);
		registerReferee(Rule.EarlierThan.class, EarlierThanReferee.class);
	}
	
	private static enum MODE{
		DEBUG, PRODUCT
	}
	
	/**
	 * ����У��ģʽΪ����ģʽ
	 * 
	 * ��ģʽ��Ч�ʵ��ڲ�Ʒ����ģʽ
	 * �������ڷ��������ж��ֶε�ע������޸��Ա����
	 */
	public final static void debugMode(){
		cachedRulesMap.clear();
		currentMode = MODE.DEBUG;
	}
	
	/**
	 * ����ģʽΪ��Ʒ����ģʽ
	 * 
	 * ��ģʽ����Ч�ʸ��ڵ���ģʽ
	 * ���ڷ��������ڼ��ע����޸Ĳ��ᱻ��������(����ʹ��{@link cachedRules(Class<?>)}���»���)
	 * 
	 */
	public final static void productMode(){
		currentMode = MODE.PRODUCT;
	}
	
	
	
	/**
	 * �Ե����ֶν���У�� ����{@code validate(bean, String, false)}
	 * 
	 * @param bean ʵ��
	 * @param fieldName ��Ҫ������ֶ�����
	 * @return
	 */
	public final static List<Failure> validate(Object bean, String fieldName) {
		return validate(bean, fieldName, false);
	}

	/**
	 * �Ե����ֶν���У��
	 * 
	 * @param fieldName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public final static List<Failure> validate(Object bean, String fieldName,
			boolean full) {
		Field field = getField(bean, fieldName);
		List<Failure> result = validateField(bean, field, full);
		return result == null ? Collections.EMPTY_LIST : result;
	}

	/**
	 * ��ȡ�ֶ�
	 * @return
	 */
	private final static Field getField(Object bean, String fieldName){
		try {
			return bean.getClass().getDeclaredField(fieldName);
		} catch (Exception e) {
			throw new IllegalArgumentException(String.format(
					"The field %s cannot read from %s", fieldName, bean
							.getClass().getName()));
		}
	}
	
	/**
	 * 
	 * ��Bean�е������ֶν���У�� ����{@code validate(bean, false)}
	 * 
	 * @see {@code validate(Object, boolean}
	 * @param bean
	 * @return
	 */
	public final static Map<String, List<Failure>> validateAll(Object bean) {
		return validateAll(bean, false);
	}

	/**
	 * ��Bean�е������ֶν���У��
	 * 
	 * @param bean ʵ��
	 * @param full �Ƿ�У�����е�ע��(false����෵��һ��ʧ�ܽ��)
	 * 
	 * @return ����������Ϣ��Map<�����ֶ���, ��Ӧ������Ϣ����>
	 * 
	 */
	public final static Map<String, List<Failure>> validateAll(Object bean,
			boolean full) {

		// ������, fieldName 2 List<errorMessage>
		Map<String, List<Failure>> validateResult = new HashMap<String, List<Failure>>();

		Field[] fields = bean.getClass().getDeclaredFields();

		// �����ȡ�ֶ�
		for (Field field : fields) {
			List<Failure> failures = validateField(bean, field, full);
			if (failures != null && failures.size() != 0) {
				validateResult.put(field.getName(), failures);
			}
		}
		return validateResult;
	}

	/**
	 * У��Field
	 */
	private final static List<Failure> validateField(Object bean, Field field,
			boolean full) {
		List<Failure> failures = null;
		// ��ȡע��
		Annotation[] annotations = getRules(bean.getClass(), field);
		if (annotations.length == 0)
			// Ϊ������validateAllʱ�Ĵ����ռ��ϵ�����, �����ﷵ��null, ���ⲿ�ж�
			return null;
		// ѭ��ע��, ����Rule�ж�
		for (Annotation annotation : annotations) {
			Referee referee = rule2RefereeMap.get(annotation.annotationType());
			if (referee != null) {
				State result = validate(bean, field, annotation, referee);
				// ��������
				if (result.success()) {
					continue;
				} else {
					if(failures == null)
						failures = new ArrayList<Failure>();
					// �ݲ�ʹ�ô�����Ϣ���ʻ�����
					failures.add(Failure.valueOf(annotation.annotationType(), result.message(), result.status()));
					// ֻ����һ������
					if (full == false)
						break;
					else
						continue;
				}
			}
		}

		return failures;
	}

	/**
	 * �Ե����ֶεĵ���ע�����У��
	 * 
	 * @param bean ʵ��
	 * @param fieldName �ֶ�����
	 * @param rule Ӧ���ڹ����ע��
	 * @throws IllegalArgumentException ��� ����û�б�ע�� / �ֶ�û����Ӵ�ע��
	 * @return
	 */
	public final static State validate(Object bean, String fieldName, Class<? extends Annotation> rule){
		Referee referee = rule2RefereeMap.get(rule);
		// ���û��ע��˹���, ���׳��쳣
		if(referee == null)
			throw new IllegalArgumentException(String.format("The rule referee <%s> does not be registered.", rule.getName()));
		
		Field field = getField(bean, fieldName);
		Annotation annotationInstance = getRule(bean.getClass(), field, rule);
		
		// ����ֶ�û��Ӧ�ô˹���, ���׳��쳣
		if(annotationInstance == null)
			throw new IllegalArgumentException(String.format("The annotation <%s> dose not be annotated on field<%s>", rule.getName(), fieldName));
		
		return validate(bean, field, annotationInstance, referee);
	}
	
	/**
	 * ����У�� 
	 */
	private final static State validate(Object bean, Field field,
			Annotation annotation, Referee referee) {
		// �������˽�г�Ա
		field.setAccessible(true);
		Object fieldValue = null;
		try {
			fieldValue = field.get(bean);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		// ����У��  �ݲ�try-catch�׳�{@code ValidateException}
		return referee.check(bean, fieldValue, annotation, field
				.getName());
	}

	/**
	 * ��ȡ��������
	 * 
	 * ����{@code Field.getAnnotations()}��Ч������, �Ὣ���򻺴�����
	 * 
	 * @param clazz
	 * @param field
	 * @return
	 */
	private static final Annotation[] getRules(Class<?> clazz, Field field){
		if(currentMode == MODE.DEBUG)
			return field.getAnnotations();
		Map<Field, Annotation[]> field2Annotations = cachedRulesMap.get(clazz);
		if(field2Annotations == null){
			field2Annotations = new ConcurrentHashMap<Field, Annotation[]>();
			cachedRulesMap.put(clazz, field2Annotations);
			return cachedRules(field, field2Annotations);
		} 
		Annotation[] annotations = field2Annotations.get(field);
		if(annotations == null){
			annotations = cachedRules(field, field2Annotations);
		}
		return annotations;
	}

	private static final Annotation[] cachedRules(Field field, Map<Field, Annotation[]> field2Annotations){
		Annotation[] annotations = field.getAnnotations();
		field2Annotations.put(field, annotations);
		return annotations;
	}
	
	/**
	 * ��ȡ��������
	 * 
	 * @param clazz
	 * @param field
	 * @param rule
	 * @return
	 */
	private static final Annotation getRule(Class<?> clazz, Field field, Class<? extends Annotation> rule){
		if(currentMode == MODE.DEBUG)
			return field.getAnnotation(rule);
		Annotation[] annotations = getRules(clazz, field);
		for(Annotation annotation : annotations)
			if(annotation.annotationType() == rule)
				return annotation;
		return null;
	}
	
	/**
	 * �ֶ���Bean�������ֶεĹ��򻺴�����
	 * ֻ����{@code MODE.PRODUCT}ģʽ��ִ��
	 * 
	 * @param clazz ���軺���JavaBean
	 * @throws IllegalStateException �������ģʽΪ{@code MODE.DEBUG} 
	 */
	public static final void cachedRules(Class<?> clazz){
		if(currentMode == MODE.DEBUG)
			throw new IllegalStateException("Current run mode<" + currentMode + "> not support to cached rules.");
		Field[] fields = clazz.getDeclaredFields();
		for(Field field : fields){
			field.setAccessible(true);
			getRules(clazz, field);
		}
	}
	
	/**
	 * ע�����У����
	 * 
	 * @param <T>
	 * @param <R>
	 * @param rule
	 * @param referee
	 */
	public final synchronized static void registerReferee(Class<? extends Annotation> rule,
			Class<? extends Referee> referee) {
		try {
			rule2RefereeMap.put(rule, referee.newInstance());
		} catch (Exception e) {
			throw new IllegalArgumentException("Found exception when initializing referee<" + referee.getName() + ">", e);
		}
	}
	
	/**
	 * У���쳣
	 * 
	 * ��У��ʱ�����쳣ʱ�׳�
	 *  
	 */
	@SuppressWarnings("unused")
	private static class ValidateException extends RuntimeException{
		private static final long serialVersionUID = 1L;
		public ValidateException(String message, Exception e) {
			super(message, e);
		}
	}
}
