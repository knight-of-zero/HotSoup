<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<s:url action="nameGetter" var="nameLink">
  <s:param name="givenName">Jerry</s:param>
</s:url>

<p><a href="${nameLink}">Hello World</a></p>