package cn.kuehne.wsg50.packets;

import cn.kuehne.wsg50.PacketID;
import cn.kuehne.wsg50.helper.AbstractCommand;
import cn.kuehne.wsg50.helper.In;
import cn.kuehne.wsg50.helper.Out;

public class GraspPartCommand extends AbstractCommand {
	private float width;
	private float speed;

	public GraspPartCommand() {
		super(PacketID.GraspPart);
	}

	@Out(1)
	public float getSpeed() {
		return speed;
	}

	@Out(0)
	public float getWidth() {
		return width;
	}

	@In(1)
	public void setSpeed(final float newSpeed) {
		speed = newSpeed;
	}

	@In(0)
	public void setWidth(final float newWidth) {
		width = newWidth;
	}
}
