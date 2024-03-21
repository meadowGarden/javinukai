import { Link } from "react-router-dom";

export function PastCompetitionListItem({ contestRecord }) {
  return (
    <Link
      to={contestRecord.id}
      className="flex py-4 shadow bg-white xl:px-3 border-white border-4 transition ease-in-out hover:border-teal-400 hover:border-4 hover:cursor-pointer my-2 rounded-md"
    >
      <div className="xl:grid xl:grid-cols-10 px-3 xl:px-0 w-full flex justify-between">
        <p className="col-span-3">{contestRecord.contestName}</p>{" "}
        <p className="col-span-5">{contestRecord.contestDescription}</p>{" "}
        <p className="col-span-1">{contestRecord.startDate}</p>{" "}
        <p className="col-span-1">{contestRecord.endDate}</p>{" "}
      </div>
    </Link>
  );
}
