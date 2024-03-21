import { useQuery } from "@tanstack/react-query";
import { useState } from "react";
import PaginationSettings from "../Components/PaginationSettings";
import { BarLoader } from "react-spinners";
import { useTranslation } from "react-i18next";
import ChangePage from "../Components/user-management/ChangePage";
import getPastCompetitionRecord from "../services/archive/getPastCompetitionRecord";
import { PastCompetitionListItem } from "../Components/archive/PastCompetitionListItem";

const defaultPagination = {
  page: 0,
  limit: 25,
  sortBy: "contestName",
  sortDesc: "false",
  searchedField: null,
};

function ArchivePage() {
  const [paginationSettings, setPaginationSettings] =
    useState(defaultPagination);
  const { data, isFetching } = useQuery({
    queryKey: [
      "archiveRecords",
      paginationSettings.page,
      paginationSettings.limit,
      paginationSettings.sortBy,
      paginationSettings.sortDesc,
      paginationSettings.searchedField,
    ],
    queryFn: () =>
      getPastCompetitionRecord(
        paginationSettings.page,
        paginationSettings.limit,
        paginationSettings.sortBy,
        paginationSettings.sortDesc,
        paginationSettings.searchedField
      ),
  });

  const { t } = useTranslation();

  return (
    <div className="w-full min-h-[82vh] xl:flex xl:flex-col xl:items-center bg-slate-50">
      <div className="xl:w-3/4 w-full px-2">
        <PaginationSettings
          pagination={paginationSettings}
          setPagination={setPaginationSettings}
          availablePageNumber={data?.totalPages}
          limitObjectName={t("UserManagementPage.userLimitObject")}
          sortFieldOptions={
            <>
              <option value="contestName">
                {t("ArchivePage.contestName")}
              </option>
            </>
          }
          searchByFieldName={t("ArchivePage.contestName")}
          firstPage={data?.firs}
          lastPage={data?.last}
        />
        <div className=" xl:grid xl:grid-cols-10 px-3 py-5 font-bold text-lg text-slate-700 bg-white mt-2 rounded-md shadow">
          <p className="col-span-3">{t("ArchivePage.contestName")}</p>
          <p className="col-span-5">{t("ArchivePage.contestDescription")}</p>
          <p className="col-span-1">{t("ArchivePage.startDate")}</p>
          <p className="col-span-1">{t("ArchivePage.endDate")}</p>
        </div>
        {isFetching ? (
          <div className="h-[50vh] flex flex-col justify-center items-center">
            <BarLoader />
          </div>
        ) : (
          <>
            {data?.content.map((contestRecord) => (
              <PastCompetitionListItem
                key={contestRecord.id}
                contestRecord={contestRecord}
              />
            ))}
          </>
        )}
      </div>
      <div>
        <ChangePage
          pagination={paginationSettings}
          setPagination={setPaginationSettings}
          availablePageNumber={data?.totalPages}
        />
      </div>
    </div>
  );
}

export default ArchivePage;
