package widgets;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class OneFieldWidget {
    private final EmbedBuilder embedBuilder;

    public OneFieldWidget(String title, String description, String fieldTitle, String field) {
        this.embedBuilder = new EmbedBuilder();
        this.embedBuilder.setTitle(title).setDescription(description).addField(fieldTitle, field, true);
    }

    public MessageEmbed build() {
        return this.embedBuilder.build();
    }
}
