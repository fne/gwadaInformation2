package fr.info.antillesinfov2.model;

import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(strict=false)
public class RSS {
	@Element
	private List<Item> channel;

	public List<Item> getChannel() {
		return channel;
	}

	public void setChannel(List<Item> channel) {
		this.channel = channel;
	}
}
