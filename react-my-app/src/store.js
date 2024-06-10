//액션 (action)
export const increase = (username) => ({
  type: 'INCREMENT',
  payload: username,
}); //객체를 반환할땐 이렇게 씀
export const decrease = () => ({ type: 'DECREMENT' });

//상태 (state)
const initstate = {
  number: 1,
  username: 'ssar',
};

// reducer (액션의 결과를 걸러내는 놈이 reducer)
const reducer = (state = initstate, action) => {
  switch (action.type) {
    case 'INCREMENT':
      return { number: state.number + 1, username: action.payload }; // 이게 return 되면 그걸 호출한 쪽에서 받는게 아니라 return되는 순간 state를 자동으로 바꿔주면서 ui가 변경됨

    case 'DECREMENT':
      return { number: state.number - 1 };

    default:
      return state;
  }
};

export default reducer;
