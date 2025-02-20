package broker.domain;

import org.apache.commons.lang3.StringUtils;

public enum PollResult {

    veryEasy, easy, neutral, hard, veryHard;

    public String getDisplayName() {
        return StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(name()), " ");
    }

}
