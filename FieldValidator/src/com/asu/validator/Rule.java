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
		// ������Ϣ���������������ʾĬ��
		String message() default "";
	}

	// equals�Ƚ�
	@Retention(RetentionPolicy.RUNTIME)
	@Target( { ElementType.FIELD })
	public static @interface Equals {
		// �Ƚϵ��ֶ���, ֻ���뵱ǰʵ�����ֶαȽ�
		String value();

		// ������Ϣ���������������ʾĬ��
		String message() default "";
	}

	// ֵ��ͬ�Ƚ�
	@Retention(RetentionPolicy.RUNTIME)
	@Target( { ElementType.FIELD })
	public static @interface NonEquals {
		// �Ƚϵ��ֶ���, ֻ���뵱ǰʵ�����ֶαȽ�
		String value();

		// ������Ϣ���������������ʾĬ��
		String message() default "";
	}

	// �ַ�������
	@Retention(RetentionPolicy.RUNTIME)
	@Target( { ElementType.FIELD })
	public static @interface Length {
		// ��С����, Ĭ��Ϊ0
		long min() default 0;

		// ��󳤶�
		long max();

		// ������Ϣ���������������ʾĬ��
		String message() default "";
	}

	// ��Ӣ��
	@Retention(RetentionPolicy.RUNTIME)
	@Target( { ElementType.FIELD })
	public static @interface English {
		// ������Ϣ���������������ʾĬ��
		String message() default "";
	}

	// ������
	@Retention(RetentionPolicy.RUNTIME)
	@Target( { ElementType.FIELD })
	public static @interface Chinese {
		// ������Ϣ���������������ʾĬ��
		String message() default "";
	}

	// ������ʽƥ��
	@Retention(RetentionPolicy.RUNTIME)
	@Target( { ElementType.FIELD })
	public static @interface Regex {
		// ������ʽ
		String value();

		// �ֶ�����
		String name();

		// ������Ϣ���������������ʾĬ��
		String message() default "";
	}

	// �ַ�������������
	@Retention(RetentionPolicy.RUNTIME)
	@Target( { ElementType.FIELD })
	public static @interface Num {
		NumberType value();

		// ������Ϣ���������������ʾĬ��
		String message() default "";
	}

	// ��������
	@Retention(RetentionPolicy.RUNTIME)
	@Target( { ElementType.FIELD })
	public static @interface NumberRange {
		// ��Сֵ Ĭ��Ϊ0
		long min() default 0;

		// ���ֵ ���С��{@code min}�����׳�IllegalArgumentException
		long max();
		
		boolean minEquals() default true;
		
		boolean maxEquals() default true;

		// ������Ϣ���������������ʾĬ��
		String message() default "";
	}

	// ���ڸ�ʽ
	@Retention(RetentionPolicy.RUNTIME)
	@Target( { ElementType.FIELD })
	public static @interface DateTime {
		// ���ڸ�ʽ�ַ���
		String[] value();

		// ������Ϣ���������������ʾĬ��
		String message() default "";
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

		// �Ƿ���Ե���
		boolean equalable() default true;

		// ������Ϣ���������������ʾĬ��
		String message() default "";
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

		// �Ƿ���Ե���
		boolean equalable() default true;

		// ������Ϣ���������������ʾĬ��
		String message() default "";
	}

	// ʱ������
	@Retention(RetentionPolicy.RUNTIME)
	@Target( { ElementType.FIELD })
	public static @interface EarlierThan {
		// OGNL���ʽ, ��ĳ���ֶν��бȽ�
		// ����ܱȽ��ֶ�ΪNull���׳�NullPointerException
		// �����ʽ��ΪDate ���׳�IllegalArgumentException
		String value();

		// ������Ϣ���������������ʾĬ��
		String message() default "";
	}

	// ʱ������
	@Retention(RetentionPolicy.RUNTIME)
	@Target( { ElementType.FIELD })
	public static @interface LaterThan {
		// OGNl���ʽ, ��ĳ���ֶν��бȽ�
		// ����ܱȽ��ֶ�ΪNull���׳�NullPointerException
		// �����ʽ��ΪDate ���׳�IllegalArgumentException
		String value();

		// ������Ϣ���������������ʾĬ��
		String message() default "";
	}
}
