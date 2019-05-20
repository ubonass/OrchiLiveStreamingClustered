package orchi.liveStreaming.streams.cluster.task;

import java.io.Serializable;
import java.util.concurrent.Callable;

import orchi.liveStreaming.streams.ManagerStream;
import orchi.liveStreaming.streams.RealStream;

public class TaskConnectRtpRefToRtpReal implements Callable<String>, Serializable {

	private String idStream;
	private String offer;
	private String idMember;

	public TaskConnectRtpRefToRtpReal(String idStream, String idMember, String offer) {
		//ambito de un pc
		ManagerStream.log.info("nueva tarea");
		this.idMember = idMember;
		this.idStream = idStream;
		this.offer = offer;
	}

	/*
	* 当Host B提交submitTaskToMembers任务并指定相应的idMember时候
	*  假设此时A收到请求,那么这段函数应该是在A host中执行,而不是在B中执行
	* */
	@Override
	public String call() throws Exception {
		// ambito del otro pc
		ManagerStream.log.info("realizando tarea en remoto");
		RealStream stream = (RealStream) ManagerStream.getInstance().getStream(idStream, false);
		if (stream == null) {
			// log.info("No existe stream en este nodo");
			throw new Exception("No existe stream en este nodo");
		}
		/**
		 * A开始处理sdpOffer并返回sdpAnswer
		 */
		String procesAnswer = stream.getRtpByHostid(this.idMember).processOffer(offer);
		return procesAnswer;
	}

}