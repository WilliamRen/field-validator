package com.asu.validator.referee;

import java.util.Date;

import com.asu.validator.AbstractCompareReferee;
import com.asu.validator.State;
import com.asu.validator.Rule.EarlierThan;

/**
 * �������ڱȽ�
 * 
 * �Ƚ������뱻�Ƚ��������Ϊ@{code java.util.Date}����, �Ҳ�Ϊnull �����׳�IllegalArgumentException
 * 
 * @author Bevis.Zhao(avengerbevis@gmail.com)
 * 
 */
public class EarlierThanReferee extends AbstractCompareReferee<EarlierThan> {

	@Override
	public State check(Object data) {
		Object target = getFieldValue(rule.value());
		// ����Ϊnull
		if (data == null || target == null)
			throw new IllegalArgumentException(String.format(
					"Both field<%s> and to-compare-field<%s> cannot be null.",
					fieldName, rule.value()));

		// ����Ϊdate����
		if (!(data instanceof Date) || !(target instanceof Date))
			throw new IllegalArgumentException(String.format(
					"Both field<%s> and to-compare-field<%s> only allowed type of java.util.Date.",
					fieldName, rule.value()));

		Date currDate = (Date) data, targetDate = (Date) target;

		if (currDate.getTime() <= targetDate.getTime())
			return simpleSuccess();
		else
			return failure("The data is not earlier than target data.");

	}

}
