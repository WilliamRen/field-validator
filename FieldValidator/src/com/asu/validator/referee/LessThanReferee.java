package com.asu.validator.referee;

import java.math.BigDecimal;

import com.asu.validator.AbstractCompareReferee;
import com.asu.validator.State;
import com.asu.validator.Rule.LessThan;

/**
 * �����ֽ���С�ڵ��ڱȽ�
 * 
 * @author Bevis.Zhao(avengerbevis@gmail.com)
 * 
 */
public class LessThanReferee extends AbstractCompareReferee<LessThan> {

	@Override
	public State check(Object data) {
		// ��������ת��
		if (!(data instanceof Number))
			throw new IllegalArgumentException(String.format(
					"The field is not type of Number.", fieldName));
		Object target = getFieldValue(rule.value());
		if (!(target instanceof Number))
			throw new IllegalArgumentException(String.format(
					"The target is not type of Number.", rule.value()));

		BigDecimal number = new BigDecimal(data + ""), targetNumber = new BigDecimal(
				target + "");

		if (number.doubleValue() < targetNumber.doubleValue())
			return simpleSuccess();
		else if (rule.equalable() && number.doubleValue() == targetNumber.doubleValue())
			return simpleSuccess();
		else
			return failure(getMessageRuleFirst("number.lessThan", "The data is not less than target data"));
	}
}
