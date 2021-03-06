package com.asu.validator.referee;

import com.asu.validator.AbstractReferee;
import com.asu.validator.State;
import com.asu.validator.Rule.Regex;

/**
 * ����У��
 * @author Bevis.Zhao(avengerbevis@gmail.com)
 *
 */
public class RegexReferee extends AbstractReferee<Regex> {

	@Override
	public State check(Object data) {
		return regexMatch(rule.value(), data, 
				String.format(getMessageRuleFirst("object.equals", "The data named %s is not match regex.")
						, rule.name()));
	}

}
