function EndContest({ contestInfo }) {
  console.log("end contest -> contest info => ", contestInfo);

  return (
    <>
      <div
        className={`${
          contestInfo ? "w-full" : "w-3/5 my-5"
        } mx-auto p-6 bg-white rounded-md shadow-md`}
      >
        {contestInfo?.id}
      </div>
    </>
  );
}

export default EndContest;
