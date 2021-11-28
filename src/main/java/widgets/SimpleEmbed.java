package widgets;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class SimpleEmbed {
    private final EmbedBuilder embedBuilder;

    public SimpleEmbed(String title, String text) {
        this.embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(title).setDescription(text);
    }

    public static SimpleEmbed createSimpleEmbed(String title, String text) {
        return new SimpleEmbed(title, text);
    }

    public SimpleEmbed setDescription(String text) {
        this.embedBuilder.setDescription(text);
        return this;
    }

    public MessageEmbed build() {
        return this.embedBuilder.build();
    }
}
