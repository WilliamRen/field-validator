package com.asu.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ������, �������ù�������ڴ�. ����Bean����ʱ����ʹ��import static ��������
 * 
 * @author Beviz
 */
public interface Rule {
	// ��������
	public static enum NumberType {
		SHORT, INTEGER, LONG, FLOAT, DOUBLE, NUMBER
	}

	// ��ֹΪ��
	@Retention(RetentionPolicy.RUNTIME)
	@Target( { ElementType.FIELD })
	public static @interface NonNull {

	}
	
	// equals�Ƚ�
	@Retention(RetentionPolicy.RUNTIME)
	@Target( { ElementType.FIELD })
	public static @interface Equals {
		// �Ƚϵ��ֶ���, ֻ���뵱ǰʵ�����ֶαȽ�
		String value();
	}
	
	// ֵ��ͬ�Ƚ�
	@Retention(RetentionPolicy.RUNTIME)
	@Target( { ElementType.FIELD })
	public static @interface NonEquals {
		// �Ƚϵ��ֶ���, ֻ���뵱ǰʵ�����ֶαȽ�
		String value();
	}
	
	// �ַ�������
	@Retention(RetentionPolicy.RUNTIME)
	@Target( { ElementType.FIELD })
	public static @interface Length {
		// ��С����, Ĭ��Ϊ0
		long min() default 0;

		// ��󳤶�
		long max();
	}


	// ��Ӣ��
	@Retention(RetentionPolicy.RUNTIME)
	@Target( { ElementType.FIELD })
	public static @interface English {
	}

	// ������
	@Retention(RetentionPolicy.RUNTIME)
	@Target( { ElementType.FIELD })
	public static @interface Chinese {
	}


	// ������ʽƥ��
	@Retention(RetentionPolicy.RUNTIME)
	@Target( { ElementType.FIELD })
	public static @interface Regex {
		// ������ʽ
		String value();
	}

	// �ַ�������������
	@Retention(RetentionPolicy.RUNTIME)
	@Target( { ElementType.FIELD })
	public static @interface Num {
		NumberType value();
	}

	// ��������
	@Retention(RetentionPolicy.RUNTIME)
	@Target( { ElementType.FIELD })
	public static @interface NumberRange {
		// ��Сֵ Ĭ��Ϊ0
		long min() default 0;

		// ���ֵ ���С��{@code min}�����׳�IllegalArgumentException
		long max();
	}

	// ���ڸ�ʽ
	@Retention(RetentionPolicy.RUNTIME)
	@Target( { ElementType.FIELD })
	public static @interface DateTime {
		// ���ڸ�ʽ�ַ���
		String[] value();
	}
	
	/**
	 * ����������͵Ĵ��ڵ��ڱȽ�
	 *
	 * @author Bevis.Zhao(avengerbevis@gmail.com)
	 * @throw IllegalArgumentException �����ǰ�����Ŀ�����Ϊ{@code Number}����
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target( { ElementType.FIELD })
	public static @interface GreaterThan {
		String value();
	}
	
	/**
	 * ����������͵�С�ڵ��ڱȽ�
	 *
	 * @author Bevis.Zhao(avengerbevis@gmail.com)
	 * @throw IllegalArgumentException �����ǰ�����Ŀ�����Ϊ{@code Number}����
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target( { ElementType.FIELD })
	public static @interface LessThan {
		String value();
	}

	// ʱ������
	@Retention(RetentionPolicy.RUNTIME)
	@Target( { ElementType.FIELD })
	public static @interface EarlierThan {
		// OGNl���ʽ, ��ĳ���ֶν��бȽ�
		// ����ܱȽ��ֶ�ΪNull���׳�NullPointerException
		// �����ʽ��ΪDate ���׳�IllegalArgumentException
		String value();
	}

	// ʱ������
	@Retention(RetentionPolicy.RUNTIME)
	@Target( { ElementType.FIELD })
	public static @interface LaterThan {
		// OGNl���ʽ, ��ĳ���ֶν��бȽ�
		// ����ܱȽ��ֶ�ΪNull���׳�NullPointerException
		// �����ʽ��ΪDate ���׳�IllegalArgumentException
		String value();
	}
}
