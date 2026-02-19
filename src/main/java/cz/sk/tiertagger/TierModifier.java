package cz.sk.tiertagger;

import net.minecraft.text.Text;

public interface TierModifier {
    Text getSuffixText();
    void setSuffixText(Text value);
}
