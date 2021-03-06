<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="bookstore.utility.*" %>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <title>聊天室</title>
  <script type="text/javascript" src='./js/websocket.js'></script>
  <link rel="stylesheet" href="./css/bootstrap.css" />
  <link rel="stylesheet" href="./css/bootstrap-theme.min.css" />
  <link rel="stylesheet" href="./css/style.css" />
</head>
<body>
    <div class="container">

	<!-- Static navbar -->
      <nav class="navbar navbar-default" id="maindir">
        <div class="container-fluid">
          <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
              <span class="sr-only">Toggle navigation</span>
              <span class="icon-bar"></span>
              <span class="icon-bar"></span>
              <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="./<%= PageName.INDEX_PG %>">主页</a>
          </div>
          <div id="navbar" class="navbar-collapse collapse">
            <ul class="nav navbar-nav">
              <!--
              <li class="active"><a href="#">Home</a></li>
              -->
            </ul>
          </div><!--/.nav-collapse -->
        </div><!--/.container-fluid -->
      </nav>  
    
    <div class="panel panel-primary">
    
    <div class="panel-heading">聊天室</div>

    <div class="panel-body">
        <div class="row">
            <div class="col-md-3">
                <input id="name" type="text" size="20" maxlength="20" class="form-control" 
                   onkeyup="checkJoin(event);" placeholder="Your name:" /> 
            </div>
            <div class="col-md-2">
               <input type="submit" id="join" value="Join!" onclick="sendJoin();" 
               class="btn btn-primary btn-block" />
             </div>
        </div>
    </div>
    
    <div class="panel-body">

        <div class="row">
            <div class="col-md-5">
                <textarea id="input" cols="70" rows="1" disabled="true" class="form-control"
                          onkeyup="sendMessage(event);"></textarea>
            </div>
        </div>
        <br />
        <div class="row">
            <div class="col-md-5">
                <textarea id="textarea" cols="70" rows="20" readonly="true" class="form-control"></textarea>
            </div>
            <div class="col-md-2">
                <textarea id="userlist" cols="20" rows="20" readonly="true" class="form-control"></textarea>
            </div>
        </div>
    
    </div>

    <div class="panel-body">
    
        <div class="row">
            <div class="col-md-1">
                <input id="showhideconsole" type="checkbox" onclick="showHideConsole();" class="form-control"/>
            </div>
            <div class="col-md-3">
                Show WebSocket console
            </div>
        </div>
        <br />
        <div class="row" id='consolediv'>
            <div class="col-md-7" id="console">
               <textarea id="wsconsole" cols="80" rows="8" readonly="true" class="form-control" 
                         style="font-size:8pt;"></textarea>
            </div>
        </div>
        
    </div>

	</div>
	
	</div>
</body>
</html>