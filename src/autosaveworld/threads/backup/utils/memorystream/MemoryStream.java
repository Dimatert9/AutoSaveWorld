/**
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 */

package autosaveworld.threads.backup.utils.memorystream;

public class MemoryStream {

	private MemoryOutputStream os = new MemoryOutputStream(this);
	public MemoryOutputStream getOutputStream() {
		return os;
	}

	private MemoryInputStream is = new MemoryInputStream(this);
	public MemoryInputStream getInputStream() {
		return is;
	}

	public void putStreamEndSignal() {
		bytequeue.put(-1);
	}

	private IntLinkedBlockingQueue bytequeue = new IntLinkedBlockingQueue(10 * 1024 * 1024);

	private boolean eof = false;

	protected int read() {
		if (eof) {
			return -1;
		}
		int r = bytequeue.take();
		if (r == -1) {
			eof = true;
		}
		return r;
	}

	protected int read(byte b[], int off, int len) {
        int c = read();
        if (c == -1) {
            return -1;
        }
        b[off] = (byte)c;

        int i = 1;
        for (; i < len ; i++) {
            c = read();
            if (c == -1) {
                break;
            }
            b[off + i] = (byte)c;
        }
        return i;
	}

	protected void write(int b) {
		bytequeue.put(b);
	}

	protected void write(byte b[], int off, int len) {
        for (int i = 0 ; i < len ; i++) {
            write(b[off + i]);
        }
	}

}
