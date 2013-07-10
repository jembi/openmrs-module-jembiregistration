<form method="post" action="${pageContext.request.contextPath}/moduleServlet/jembiregistration/printBarCodeServlet">
	<input type="hidden" name="redir" value="${pageContext.request.contextPath}/patientDashboard.form?patientId=<%=request.getParameter("patientId")%>"/>
	<input type="hidden" name="patientId" value="<%=request.getParameter("patientId")%>"/>
	<input type="submit" value='<spring:message code="jembiregistration.printBarCode" />'/>
</form>