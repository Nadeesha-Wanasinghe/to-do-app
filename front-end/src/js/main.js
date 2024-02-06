const txtElm = document.querySelector("#txt");
const btnAddElm = document.querySelector("#btn-add");
const taskContainerElm = document.querySelector("#task-container");

loadAllTasks();

function loadAllTasks(){
    // Todo: Retreive all tasks from the back-end
    const taskList = [
        {id: 1, description: 'Task 1', status: false},
        {id: 2, description: 'Task 2', status: true},
        {id: 3, description: 'Task 3', status: true},
        {id: 4, description: 'Task 4', status: false},
    ];
    taskList.forEach(task => createTask(task));
}

function createTask(task){
    const liElm = document.createElement('li');
    taskContainerElm.append(liElm);

    liElm.innerHTML = `
        <input id="chk-task-${task.id}" type="checkbox" ${task.status ? "checked": ""}>
        <label for="chk-task-${task.id}">${task.description}</label>
        <i class="delete bi bi-trash"></i>    
    `;
}

taskContainerElm.addEventListener('click', (e)=>{
    if (e.target?.classList.contains('delete')){
        // Todo: Delete the task from the back-end
        // If success
        e.target.closest("li").remove();
    } else if (e.target?.tagName === "INPUT"){
        // Todo: Update the task status in the back-end
        // If not success
        if (!false){
            e.preventDefault();
        }
    }
});

let taskId = 0;

btnAddElm.addEventListener('click', ()=>{
    const taskDescription = txtElm.value;
    
    if (!taskDescription.trim()){
        txtElm.focus();
        txtElm.select();
        return;
    }

    // Todo: Save the task in the back-end
    // If success

    createTask({id: taskId++, description: taskDescription, status: false});
    txtElm.value = "";
    txtElm.focus();
});