/*
 * Copyright 2015 Red Hat Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
var container = require('rhea');
var fs = require('fs');
var path = require('path');
var args = require('./options.js').options({
      'm': { alias: 'messages', default: 10, describe: 'number of messages to send'},
      'n': { alias: 'node', default: 'examples', describe: 'name of node (e.g. queue) to which messages are sent'},
      'p': { alias: 'port', default: 5672, describe: 'port to connect to'}
    }).help('help').argv;

var confirmed = 0, sent = 0;
var total = args.messages;

container.on('sendable', function (context) {
    while (context.sender.sendable() && sent < total) {
        sent++;
        console.log('sent ' + sent);
        context.sender.send({message_id:sent, body:{'sequence':sent}})
    }
});
container.on('accepted', function (context) {
    if (++confirmed === total) {
        console.log("all messages confirmed")
        context.connection.close()
    }
});
container.on('disconnected', function (context) {
    sent = confirmed;
});

console.log("before connect");
// container.connect({port:args.port, host:"messaging-jjeyaytu2g-amq-online-infra.apps.678b.openshift.opentlc.com", username:'user1', password:'password', transport:'tls',rejectUnauthorized:false}).open_sender({target:args.node, snd_settle_mode:0});

//container.connect({port:args.port, host:"amq-interconnect-irpubsub.apps-30ff.generic.opentlc.com", transport:'tls',rejectUnauthorized:false}).open_sender({target:args.node, snd_settle_mode:0});

//container.connect({port:args.port, host:"master00-59e7.generic.opentlc.com", transport:'tls',rejectUnauthorized:false}).open_sender({target:args.node, snd_settle_mode:0});

container.connect({
	port:args.port, 
	    host:"master00-59e7.generic.opentlc.com", 
	    //	    host:"messaging-8yctk1jkeq-enmasse.apps.scl-449e.openshiftworkshop.com",
	    // These are necessary only if using the client certificate authentication
	    //key: fs.readFileSync(path.resolve('/Users/nandanjoshi/tmp/keys/','client-key.pem')),
	    //cert: fs.readFileSync(path.resolve('/Users/nandanjoshi/tmp/keys/','client-cert.pem')),

	    // This is necessary only if the server uses the self-signed certificate
	    //ca: [ fs.readFileSync(path.resolve('/Users/nandanjoshi/tmp/keys','ca-cert.pem')) ],
	    //	    username:'admin', password:'QyON02TN', transport:'tls',
	    //	    transport:'tls',
	    rejectUnauthorized:false})
    .open_sender({target:args.node, snd_settle_mode:0});