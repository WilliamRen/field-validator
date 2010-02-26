package com.asu.validator.referee;

import com.asu.validator.AbstractReferee;
import com.asu.validator.State;
import com.asu.validator.Rule.URL;

/**
 * URL У��
 * @author Bevis.Zhao(avengerbevis@gmail.com)
 * @deprecated ������ʽ��������, ������֮ǰ����ʹ��RegexReferee
 */
public class URLReferee extends AbstractReferee<URL> {

	@Override
	public State check(Object data) {
		// FIXME ����������
		return regexMatch("^[a-zA-z]+://(\\w+(-\\w+)*)(\\.(\\w+(-\\w+)*))*(\\?\\S*)?$",
				data, "The data is not a url(start with http:// or https://)");
	}
}
