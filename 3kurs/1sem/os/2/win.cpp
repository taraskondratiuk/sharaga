#include <windows.h>
#include <stdio.h>
#include <tchar.h>
#include <psapi.h>
#include <iostream>
#include <pdh.h>

#pragma comment(lib,"pdh.lib")

#define DIV 1024

#define WIDTH 7

using namespace std;

class ProcInfo {
public:
	TCHAR processName[MAX_PATH] = TEXT("<unknown>");
	DWORD pid;
	FILETIME ftKernelStart,
		ftUserStart,
		ftSysKernelStart,
		ftSysUserStart,
		ftKernelEnd,
		ftUserEnd,
		ftSysKernelEnd,
		ftSysUserEnd;
	PROCESS_MEMORY_COUNTERS pmc;

	void printProcInfo() {
		
		cout<<endl << " " << pid << "\t";
		cout <<getCpuUsage()<<" %    \t\t" ;
		cout << pmc.PeakWorkingSetSize / (DIV * DIV) << " mb \t\t\t";
		cout << pmc.WorkingSetSize / (DIV * DIV) << " mb\t\t";
		cout << pmc.PagefileUsage / (DIV * DIV) << " mb \t\t";
		cout << pmc.PeakPagefileUsage / (DIV * DIV) << " mb \t\t";
		wprintf(processName);
	}
	static void printTableHead() {
		cout << " pid | cpu usage | peak working set size |";
		cout << " working set size | page file usage | peak page file usage | name" << endl;
	}

private:
	int getCpuUsage() {
		ULONGLONG sysTime = ((convert(ftSysKernelEnd) + convert(ftSysUserEnd))
			- (convert(ftSysKernelStart) + convert(ftSysUserStart)));
		ULONGLONG procTime = convert(ftKernelEnd) - convert(ftKernelStart) + convert(ftUserEnd) - convert(ftUserStart);
		
		return  ((200 * procTime) / sysTime);
	}

	ULONGLONG convert(FILETIME ft) {

		return ((((ULONGLONG)ft.dwHighDateTime) << 32) + ft.dwLowDateTime);
	}

	

};
void printMemoryInfo() {
	MEMORYSTATUSEX statex;

	statex.dwLength = sizeof(statex);

	GlobalMemoryStatusEx(&statex);
	cout << "Memory in use: " << statex.dwMemoryLoad << "%" << endl;
	cout << "Total memory: " << statex.ullTotalPhys / (DIV * DIV) << "Mb" << endl;
	cout << "Available memory: " << statex.ullAvailPhys / (DIV * DIV) << "Mb" << endl;
	cout << "Total paging file: " << statex.ullTotalPageFile / (DIV * DIV) << "Mb" << endl;
	cout << "Available paging file: " << statex.ullAvailExtendedVirtual / (DIV * DIV) << "Mb" << endl;

}

void printProcessInfo(DWORD processID)
{
	ProcInfo proc_info;
	proc_info.pid = processID;
	

	HANDLE hProcess = OpenProcess(PROCESS_QUERY_INFORMATION |
		PROCESS_VM_READ,
		FALSE, processID);
	
	FILETIME ftCreation,
		ftExit,
		ftSysIdle;
	
	
	if (!GetProcessMemoryInfo(hProcess, &proc_info.pmc, sizeof(proc_info.pmc))) {
		proc_info.pmc.PeakWorkingSetSize = 0;
		proc_info.pmc.WorkingSetSize = 0;
		proc_info.pmc.PagefileUsage = 0;
		proc_info.pmc.PeakPagefileUsage = 0;
	}
	
	if (GetProcessTimes(hProcess, &ftCreation, &ftExit, &proc_info.ftKernelStart, &proc_info.ftUserStart)) {
		GetSystemTimes(&ftSysIdle, &proc_info.ftSysKernelStart, &proc_info.ftSysUserStart);
		Sleep(2000);
		GetProcessTimes(hProcess, &ftCreation, &ftExit, &proc_info.ftKernelEnd, &proc_info.ftUserEnd);
		GetSystemTimes(&ftSysIdle, &proc_info.ftSysKernelEnd, &proc_info.ftSysUserEnd);

		HMODULE hMod;
		DWORD cbNeeded;

		EnumProcessModules(hProcess, &hMod, sizeof(hMod),
			&cbNeeded);

		GetModuleBaseName(hProcess, hMod, proc_info.processName,
			sizeof(proc_info.processName) / sizeof(TCHAR));

		proc_info.printProcInfo();
	}
	

	CloseHandle(hProcess);
}

int main(void)
{
	DWORD processes_arr[1024], processes_bytes_num, num_processes;
	unsigned int i;

	EnumProcesses(processes_arr, sizeof(processes_arr), &processes_bytes_num);

	num_processes = processes_bytes_num / sizeof(DWORD);

	
	printMemoryInfo();
	ProcInfo::printTableHead();
	for (i = 0; i < num_processes; i++)
	{
		printProcessInfo(processes_arr[i]);
	}

	return 0;
}