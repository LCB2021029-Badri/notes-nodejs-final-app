// making mongoose model
// defining schema
const mongoose = require('mongoose');
const noteSchema = mongoose.Schema({
    id: {
        type:String,
        unique:true,
        required: true,
    },
    userid: {
        type:String,
        required:true,
    },
    title: {
        type:String,
        required:true,
    },
    content: {
        type:String,
    },
    dateAdded: {
        type:Date,
        default: Date.now,
    },
});

//making model using the schema
module.exports = mongoose.model("Note",noteSchema);