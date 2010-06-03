package com.asu.validator.referee;

import com.asu.validator.AbstractReferee;
import com.asu.validator.State;
import com.asu.validator.Rule.Chinese;

/**
 * ����У����
 * 
 * ���ֵ�Ƿ�Ϊ����
 * 
 * @author Bevis.Zhao(avengerbevis@gmail.com)
 *
 */
public class ChineseReferee extends AbstractReferee<Chinese> {

	@Override
	public State check(Object data) {
		return regexMatch("^[\\u0391-\\uFFE5\\s\\t\\n\\x0B\\f\\r]+$", data, 
				getMessageRuleFirst("string.chinese", "The value is not chinese"));
	}

}
