var express = require('express');
var ejs = require('ejs');
var http = require('http');
var path = require('path');
var program = require('commander');
var spawn = require('child_process').spawn;
var routes = require('./routes/main');

var app = express();

program
  .version('0.0.1')
  .option('-a, --ipaddress [value]', 'Host IP Address')
  .option('-p, --port [value]', 'Port Number')
  .option('-b, --webbrowser [value]', 'WebBrowser Path')
  .parse(process.argv);

app.configure(function(){
	app.set('port', process.env.PORT || program.port);
	app.set('view engine', 'ejs');
	app.set('view options', {layout: false});
	app.set('views', __dirname + '/views');
	app.use(express.favicon());
	app.use(express.logger('dev'));
	app.use(express.bodyParser());
	app.use(express.methodOverride());
	app.use(app.router);
	app.use(express.static(path.join(__dirname, 'public')));
});

app.engine('ejs', require('ejs').renderFile);

app.configure('development', function(){
	app.use(express.errorHandler());
});

app.get('/', routes.index);
app.post('/post', routes.post);

var args = new Array();
args.push('http://' + program.ipaddress + ':' + program.port);
var webbrowser  = spawn(program.webbrowser, args);

http.createServer(app).listen(app.get('port'), function(){
	console.log("Express server listening on port " + app.get('port'));
});
