package com.gmail.nossr50.runnables.skills;

import com.gmail.nossr50.MMOTestEnvironment;
import com.gmail.nossr50.datatypes.experience.XPGainReason;
import com.gmail.nossr50.datatypes.experience.XPGainSource;
import com.gmail.nossr50.datatypes.player.McMMOPlayer;
import com.gmail.nossr50.datatypes.skills.PrimarySkillType;
import com.gmail.nossr50.util.PaperUtil;
import java.util.logging.Logger;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.mockito.Mockito.*;

class AwardCombatXpTaskTest extends MMOTestEnvironment {
    @BeforeEach void setup() throws Exception {
        mockBaseEnvironment(Logger.getLogger(getClass().getName()));
    }

    @AfterEach void tearDown() {
        cleanUpStaticMocks();
    }

    @ParameterizedTest
    @CsvSource({"TAMING,true,0", "TAMING,false,1", "SWORDS,true,1"})
    void checksOwnerBeforeSharingCombatExperience(PrimarySkillType skill, boolean idle, int awards) {
        var owner = mock(McMMOPlayer.class);
        var player = mock(Player.class);
        when(owner.getPlayer()).thenReturn(player);
        when(experienceConfigInstance.getTamingCombatIdleSeconds()).thenReturn(300);
        try (var paper = mockStatic(PaperUtil.class)) {
            paper.when(() -> PaperUtil.idleFor(player, 300)).thenReturn(idle);
            var task = new AwardCombatXpTask(owner, skill, 1, mock(LivingEntity.class), XPGainReason.PVE);
            task.awardExperience(15);
            verify(owner, times(awards)).beginXpGain(skill, 15, XPGainReason.PVE, XPGainSource.SELF);
        }
    }
}
