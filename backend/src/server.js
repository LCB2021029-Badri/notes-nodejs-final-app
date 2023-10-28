// Initialization----------------------------------------------
const express = require('express');
const app = express();
const mongoose = require('mongoose');
const bodyParser = require('body-parser');
const Note = require('./models/Note');


const mongoDbPath = 'mongodb+srv://badriakkala:badriakkala@cluster0.3ruzxhv.mongodb.net/?retryWrites=true&w=majority';
mongoose.connect(mongoDbPath).then(function(){
// Routes------------------------------------------------------
    app.get("/",function(req,res){
        const response = { message: "badri akkala - home page"};
        res.json(response);
    });


    // from NoteRoutes 
    const noteRouter = require('./routes/NoteRoutes');
    app.use("/notes",noteRouter); ///notes is appended to others in noteRoutes

});



// Starting the server in a PORT-------------------------------
app.use(bodyParser.urlencoded({extended:false}))
app.use(bodyParser.json());
// const PORT = 8100
const PORT = process.env.PORT || 10000;
app.listen(PORT, function(){
    // callback function
    console.log("Console started at PORT: " + PORT);
});


//deploy this in render or heroku (paid)
//render url : https://notes-nodejs-server-badri.onrender.com