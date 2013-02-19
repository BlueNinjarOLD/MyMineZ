package me.kitskub.myminez.command.user;

import me.kitskub.myminez.CommandPerms.Perm;
import me.kitskub.myminez.GameManager;
import me.kitskub.myminez.command.PlayerCommand;
import me.kitskub.myminez.Configs.Config;
import me.kitskub.myminez.stats.PlayerStat;
import me.kitskub.myminez.stats.PlayerStat.Team;
import me.kitskub.myminez.utils.ChatUtils;

import org.bukkit.entity.Player;

public class TeamCommand extends PlayerCommand {

	public TeamCommand() {
		super(Perm.TEAM_COMMAND, "team", USER_COMMAND);
	}

	@Override
	public void handlePlayer(Player player, String label, String[] args) {
		game = GameManager.INSTANCE.getRawSession(player);
		if (game == null) {
			ChatUtils.error(player, "You are not in a game!");
			return;
		}
		if (!Config.ALLOW_TEAM.getBoolean(game.getSetup())) {
			ChatUtils.error(player, "Teams are not enabled for this game!");
			return;
		}
		String team = null;
		if (args.length >= 1) {
			team = args[0];
		}
		PlayerStat stat = game.getPlayerStat(player);
		boolean require = false;
		if (stat.getTeam() != null) {
			stat.setTeam(null);
		} else {
			require = true;
		}
		if (team == null) {
			if (require) {
				ChatUtils.error(player, "Must specify a team!");
			}
		} else {
			game.getPlayerStat(player).setTeam(Team.get(team));
		}
	}

	@Override
	public String getInfo() {
		return "joins the team specified (may create a new one if there is nobody in it) or leaves current team";
	}

	@Override
	protected String getPrivateUsage() {
		return "team <team>";
	}
}
