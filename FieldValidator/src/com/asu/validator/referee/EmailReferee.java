package com.asu.validator.referee;

import com.asu.validator.AbstractReferee;
import com.asu.validator.State;
import com.asu.validator.Rule.Email;

/**
 * Email��ַУ��
 * 
 * @author Bevis.Zhao(avengerbevis@gmail.com)
 * @deprecated ������ʽ��������, ������֮ǰ����ʹ��RegexReferee
 */
public class EmailReferee extends AbstractReferee<Email> {

	@Override
	public State check(Object data) {
		// FIXME ����������
		return regexMatch("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*",
				data, "The data is not an E-mail address.");
	}

}
