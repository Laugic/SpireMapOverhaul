package spireMapOverhaul.zones.volatileGrounds.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.SuicideAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import spireMapOverhaul.abstracts.AbstractSMOPower;
import spireMapOverhaul.zones.volatileGrounds.VolatileGrounds;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;

public class EruptPower extends AbstractSMOPower {
    public static final String ID = makeID("volEruptPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public static String ZONE_ID = VolatileGrounds.ID;
    
    public EruptPower(AbstractCreature owner, int amount, int amount2) {
        super(ID, NAME, ZONE_ID, PowerType.BUFF, false, owner, amount);
        isTwoAmount = true;
        this.amount2 = amount2;
        updateDescription();
    }
    @Override
    public void updateDescription() {
        if(amount > 1) {
            this.description = DESCRIPTIONS[0].replace("{0}", this.amount+ "")
                    .replace("{1}", this.amount2 + "");
            return;
        }
        this.description = DESCRIPTIONS[1].replace("{0}", this.amount + "")
                .replace("{1}", this.amount2 + "");
    }
    
    
    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        if (this.amount == 1 && !this.owner.isDying) {
            this.addToBot(new VFXAction(new ExplosionSmallEffect(this.owner.hb.cX, this.owner.hb.cY), 0.1F));
            this.addToBot(new SuicideAction((AbstractMonster) this.owner));
            DamageInfo damageInfo = new DamageInfo(this.owner, amount2, DamageInfo.DamageType.THORNS);
            this.addToBot(new DamageAction(AbstractDungeon.player, damageInfo, AbstractGameAction.AttackEffect.FIRE, true));
        }
        else
        {
            AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(owner, owner, this, 1));
        }
    }
    

}
