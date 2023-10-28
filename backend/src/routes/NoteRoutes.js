const express = require('express');
const router = express.Router(); // const app = express() is replaced since app is only in server.js
const Note = require('./../models/Note');

router.get("/list", async function(req,res){
    // show all notes in database
    var notes = await Note.find();
    res.json(notes);
});

// app.get("/notes/list/:userid", async function(req,res){
//     // filter notes based on email.id (param)
//     var notes = await Note.find({
//         userid: req.params.userid
//     });
//     res.json(notes);
// });

router.post("/list", async function(req,res){
    // filter notes based on email.id (body)
    var notes = await Note.find({
        userid: req.body.userid
    });
    res.json(notes);
});


router.post("/add", async function(req,res){
    // add new note in database
    // if exists delete first before adding
    // var newNote = new Note({id: "0003",userid: "eren3@gmail.com",title: "my third note",content: "this is the content"});

    await Note.deleteOne({id: req.body.id});

    var newNote = new Note({
        id: req.body.id,
        userid: req.body.userid,
        title: req.body.title,
        content: req.body.content,
    });
    
    await newNote.save();

    const response = { message: "New Note Created! " + `id: ${req.body.id}`};
    res.json(response);
});


router.post("/delete", async function(req,res){
    // delete existing note in database
    await Note.deleteOne({
        id: req.body.id
    });

    const response = { message: "Note Deleted! " + `id: ${req.body.id}`};
    res.json(response);
});


module.exports = router;