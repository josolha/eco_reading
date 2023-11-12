function deleteBoard(boardId) {
fetch(`/board/delete/${boardId}`, {
    method: 'DELETE'
})
    .then(response => {
        if (response.ok) {
            
        } else {

        }
    })
}
